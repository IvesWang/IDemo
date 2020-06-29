package cc.ives.aeg.ui;

import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import cc.ives.aeg.AEGContext;
import cc.ives.aeg.annotation.EntryClassInfo;
import cc.ives.aeg.util.AegHelper;
import cc.ives.aeg.util.JLog;

/**
 * @author wangziguang
 * @date 2020/5/23 0023
 * @description
 */
public class AutoEntryOldListFragment extends ListFragment {
    private static final String TAG = AutoEntryOldListFragment.class.getSimpleName();

    private Class preEntryClz;// 本fragment展示的前一个操作入口，首个fragment则为null
    private List<EntryClassInfo> entryClassInfoList;

    private UIAction uiAction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AEGContext.setAppContext(getActivity().getApplicationContext());
        readPreEntryInfo();
        uiAction = new UIAction();
    }

    private void readPreEntryInfo(){
        Bundle argumentBundle = getArguments();
        if (argumentBundle != null){
            preEntryClz = (Class) argumentBundle.get(UIAction.KEY_ARGUMENT_PRE_ENTRY_CLZ);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        new Thread(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                entryClassInfoList = preEntryClz == null ? AegHelper.getEntryClassListSync() : AegHelper.getEntryClassListSync(preEntryClz);

                List<String> classDescList = entryClassInfoList.stream().map(new Function<EntryClassInfo, String>() {
                    @Override
                    public String apply(EntryClassInfo entryClassInfo) {
                        // 优先返回desc，否则返回类名
                        if (TextUtils.isEmpty(entryClassInfo.getDesc())){
                            return entryClassInfo.getCurrentClz().getSimpleName();
                        }
                        return entryClassInfo.getDesc();
                    }
                }).collect(Collectors.<String>toList());// !!!<String>

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, classDescList);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        setListAdapter(arrayAdapter);
                    }
                });
            }
        }).start();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        EntryClassInfo entryClassInfo = entryClassInfoList.get(position);
        JLog.i(TAG, String.format("onListItemClick() 点击了:%s", entryClassInfo.getCurrentClz().getCanonicalName()));

        uiAction.onItemClick(entryClassInfo, getActivity(), getFragmentManager());
    }
}
