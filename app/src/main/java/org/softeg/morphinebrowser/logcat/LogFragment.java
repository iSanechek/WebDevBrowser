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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ShareActionProvider;

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

        setHasOptionsMenu(true);
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

//        filter(Priority.VERBOSE);
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//
//        inflater.inflate(R.menu.org.softeg.morphinebrowser.logcat, menu);
//    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//
//        MenuItem item = menu.findItem(R.id.action_loglevel);
//
//        if(item == null)
//            throw new UnsupportedOperationException("loglevel menu item can't be found!");
//
//        item.setTitle(getString(R.string.loglevel) + ": " + mLogLevel);
//
//        switch (mLogLevel) {
//            case VERBOSE:
//                checkItem(menu, R.id.action_loglevel_verbose);
//                break;
//            case DEBUG:
//                checkItem(menu, R.id.action_loglevel_debug);
//                break;
//            case INFO:
//                checkItem(menu, R.id.action_loglevel_info);
//                break;
//            case WARNING:
//                checkItem(menu, R.id.action_loglevel_warn);
//                break;
//            case ERROR:
//                checkItem(menu, R.id.action_loglevel_error);
//                break;
//            case FATAL:
//                checkItem(menu, R.id.action_loglevel_assert);
//                break;
//            default:
//                item.setTitle(R.string.loglevel);
//                break;
//        }
//    }

    private void checkItem(Menu menu, int id) {
        MenuItem item = menu.findItem(id);
        if(item == null)
            throw new IllegalStateException("Menu item was not found!");

        item.setChecked(true);
    }

    private void filter(Priority priority) {
        mLogLevel = priority;
        loadItems();
//        Activity activity = getActivity();
//        if(activity != null)
//            activity.invalidateOptionsMenu();
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.action_loglevel_verbose) {
//            filter(Priority.VERBOSE);
//            item.setChecked(true);
//            return true;
//        } else if(item.getItemId() == R.id.action_loglevel_debug) {
//            filter(Priority.DEBUG);
//            item.setChecked(true);
//            return true;
//        } else if(item.getItemId() == R.id.action_loglevel_info) {
//            filter(Priority.INFO);
//            item.setChecked(true);
//            return true;
//        } else if(item.getItemId() == R.id.action_loglevel_warn) {
//            filter(Priority.WARNING);
//            item.setChecked(true);
//            return true;
//        } else if(item.getItemId() == R.id.action_loglevel_error) {
//            filter(Priority.ERROR);
//            item.setChecked(true);
//            return true;
//        } else if(item.getItemId() == R.id.action_loglevel_assert) {
//            filter(Priority.FATAL);
//            item.setChecked(true);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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

//    @Override
//    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//        try {
//            mShareActionProvider.setShareIntent(getShareIntent());
//        }
//        catch (Exception e) {
//            Log.e("LogFragment", "Error when creating intent for shareActionProvider");
//        }
//    }
//
//    @Override
//    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//        Activity activity = getActivity();
//        if(activity == null)
//            return false;
//
//        MenuInflater inflater = activity.getMenuInflater();
//        inflater.inflate(R.menu.logcat_actionmode, menu);
//
//        MenuItem item = menu.findItem(R.id.action_share);
//        if(item == null)
//            throw new IllegalStateException("share action not in menu!");
//
//        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//        return false;
//    }
//
//    @Override
//    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//        return false;
//    }
//
//    @Override
//    public void onDestroyActionMode(ActionMode mode) {
//
//    }
}
