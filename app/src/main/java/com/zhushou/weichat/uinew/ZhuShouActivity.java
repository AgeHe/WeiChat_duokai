package com.zhushou.weichat.uinew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lbsw.stat.LBStat;
import com.zhushou.weichat.R;
import com.zhushou.weichat.base.BaseFragmentActivity;
import com.zhushou.weichat.uinew.fragment.HomeFragment;
import com.zhushou.weichat.uinew.fragment.MyFragment;
import com.zhushou.weichat.uinew.fragment.NewsFragment;
import com.zhushou.weichat.uinew.fragment.StoryFragment;

/**
 * Created by Administrator on 2017/11/27.
 */

public class ZhuShouActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{

    private Fragment currShowFragment;
    private HomeFragment homeFragment;
    private MyFragment myFragment;
    private NewsFragment newsFragment;
    private StoryFragment storyFragment;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_november_main);
    }

    private RadioGroup rg_touchitem_group;
    @Override
    public void initView() {
        rg_touchitem_group = (RadioGroup) findViewById(R.id.rg_touchitem_group);
        rg_touchitem_group.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        rg_touchitem_group.check(R.id.rb_touchitem_home);
        setPermission();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.rb_touchitem_home:
                if (homeFragment==null){
                    homeFragment = new HomeFragment();
                }
                switchContent(homeFragment,"homeFragment");
                currShowFragment = homeFragment;
                break;
            case R.id.rb_touchitem_story:
                if (storyFragment==null){
                    storyFragment = new StoryFragment();
                }
                switchContent(storyFragment,"storyFragment");
                currShowFragment = storyFragment;
                LBStat.collect("广告","小说页面");
                break;
            case R.id.rb_touchitem_news:
                if (newsFragment==null){
                    newsFragment = new NewsFragment();
                }
                switchContent(newsFragment,"newsFragment");
                currShowFragment = newsFragment;
                LBStat.collect("广告","新闻页面");
                break;
            case R.id.rb_touchitem_my:
                if (myFragment==null){
                    myFragment = new MyFragment();
                }
                switchContent(myFragment,"myFragment");
                currShowFragment = myFragment;
                break;
        }
    }

    public void switchContent(Fragment to, String tag) {
        if (currShowFragment == null || !currShowFragment.getClass().getName().equals(to.getClass().getName())) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (currShowFragment == null) {
                transaction.replace(R.id.vp_main_frame, to, tag);
            } else if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(currShowFragment).add(R.id.vp_main_frame, to, tag); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(currShowFragment).show(to); // 隐藏当前的fragment，显示下一个
            }
            transaction.commit();
        }
    }

    //记录用户首次点击返回键的时间
    private long firstTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                Toast.makeText(mActivity,"再按一次回到桌面",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }else{
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// ???
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK||requestCode==10&&data!=null){
            if (homeFragment!=null){
                homeFragment.onActivityResult(requestCode,resultCode,data);
            }
        }
    }
}
