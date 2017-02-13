package org.softeg.morphinebrowser.logcat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ShareActionProvider;

import com.afollestad.materialdialogs.MaterialDialog;

import org.softeg.morphinebrowser.R;

import java.util.ArrayList;
import java.util.List;

public class LogFragment extends ListFragment implements LogHandler.OnLogItemReadListener, AbsListView.MultiChoiceModeListener {
    private static final String ARG_LOG_LEVEL = "loglevel";
    private LogHandler mLogHandler;
    private LogAdapter mLogAdapter;
    private Priority mLogLevel;
    private ShareActionProvider mShareActionProvider;
    private LogTextFactory mLogTextFactory;
    private LogFilter mLogFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLogTextFactory = new LogcatTextFactory();

        if(savedInstanceState != null) {
            mLogLevel = Priority.valueOf(savedInstanceState.getString(ARG_LOG_LEVEL));
        }
        else {
            mLogLevel = Priority.VERBOSE;
        }
        huemoe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_LOG_LEVEL, mLogLevel.name());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();
        if(context != null) {
            context.bindService(new Intent(context, LogHandlerService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLogHandler = ((LogHandlerService.LogHandlerServiceBinder) service).getLogHandler();
            mLogFilter = new LogPriorityFilter(mLogHandler);

            loadItems();

            mLogHandler.addOnLogItemReadListener(LogFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLogHandler.removeOnLogItemReadListener(LogFragment.this);
            mLogHandler = null;
        }
    };

    private void loadItems() {
        if(mLogAdapter == null) {
            mLogAdapter = new LogAdapter(getActivity(), mLogFilter.getFilteredItems(mLogLevel));
            setListAdapter(mLogAdapter);

            ListView listView = getListView();
            if(listView != null) {
                listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
                listView.setMultiChoiceModeListener(this);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                listView.setSelector(R.drawable.list_selector_holo_light);
            }
        }
        else {
            mLogAdapter.setItems(mLogFilter.getFilteredItems(mLogLevel));
            mLogAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        Context context = getActivity();
        if(context != null) {
            context.unbindService(mServiceConnection);
        }

        super.onDestroy();
    }

    @Override
    public void onLogItemRead(LogItem item) {
        if(isAdded()) {
            loadItems();
        }
    }

    private void huemoe() {
        if (getActivity().findViewById(R.id.bottom_container).getVisibility() == View.VISIBLE) {
            getActivity().findViewById(R.id.update_log_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadItems();
                }
            });

            getActivity().findViewById(R.id.log_prioritet_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialDialog.Builder(getActivity())
                            .title("Приоритет")
                            .items(R.array.logPrioritet)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                    showToast(which + ": " + text);
                                    switch (which) {
                                        case 0:
                                            filter(Priority.VERBOSE);
                                            break;
                                        case 1:
                                            filter(Priority.DEBUG);
                                            break;
                                        case 2:
                                            filter(Priority.INFO);
                                            break;
                                        case 3:
                                            filter(Priority.WARNING);
                                            break;
                                        case 4:
                                            filter(Priority.ERROR);
                                            break;
                                        case 5:
                                            filter(Priority.FATAL);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            })
                            .show();
                }
            });
        }
    }

    private void filter(Priority priority) {
        mLogLevel = priority;
        loadItems();
    }


    protected List<LogItem> getCheckedItems() throws IllegalStateException {
        ListView listView = getListView();

        if(listView == null)
            throw new IllegalStateException("List view is null");

        List<LogItem> checkedItems = new ArrayList<LogItem>();
        SparseBooleanArray positions = listView.getCheckedItemPositions();
        if(positions == null)
            throw new IllegalStateException("checkedItemPositions() returns null");

        for(int i=0;i<mLogAdapter.getCount();i++) {
            if(positions.get(i, false)) {
                checkedItems.add(mLogAdapter.getItem(i));
            }
        }

        return checkedItems;
    }

    private Intent getShareIntent() {
        List<LogItem> checkedItems = getCheckedItems();
        String text = mLogTextFactory.create(checkedItems);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Error report");
        intent.setType("text/plain");

        return intent;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
}
