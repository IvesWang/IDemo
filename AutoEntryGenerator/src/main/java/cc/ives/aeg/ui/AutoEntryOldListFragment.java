package cc.ives.aeg.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

    private List<EntryClassInfo> entryClassInfoList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AEGContext.setAppContext(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        new Thread(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                entryClassInfoList = AegHelper.getEntryClassListSync();

                List<String> classDescList = entryClassInfoList.stream().map(new Function<EntryClassInfo, String>() {
                    @Override
                    public String apply(EntryClassInfo entryClassInfo) {
                        // 优先返回desc，否则返回类名
                        if (TextUtils.isEmpty(entryClassInfo.getDesc())){
                            return entryClassInfo.getPresentClass().getSimpleName();
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
        JLog.i(TAG, String.format("onListItemClick() 点击了:%s", entryClassInfo.getPresentClass().getCanonicalName()));

        // activity则startActivity，否则找该类的入口点击方法
        if (isChild(entryClassInfo.getPresentClass(), Activity.class) || isChild(entryClassInfo.getPresentClass(), FragmentActivity.class)) {
            startActivity(new Intent(getActivity(), entryClassInfo.getPresentClass()));
        }else {
            AegHelper.invokeEntryMethod(entryClassInfo.getPresentClass());
        }
    }

    private boolean isChild(Class child, Class parent){
        return child != null
                && parent != null
                && child.getSuperclass() != null
                && (child.getSuperclass().equals(parent) || isChild(child.getSuperclass(), parent));
    }
}
