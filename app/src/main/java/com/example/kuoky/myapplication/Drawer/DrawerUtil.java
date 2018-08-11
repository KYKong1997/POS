package com.example.kuoky.myapplication.Drawer;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.kuoky.myapplication.Inventory;
import com.example.kuoky.myapplication.MainActivity;
import com.example.kuoky.myapplication.MainMenu;
import com.example.kuoky.myapplication.PosActivity;
import com.example.kuoky.myapplication.R;
import com.kinvey.android.Client;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class DrawerUtil {

    public static String userEmail="";
    public static String usernname="";

    public static void getDrawer(final Activity activity, android.support.v7.widget.Toolbar toolbar){

        String name=toolbar.getTitle().toString();
        PrimaryDrawerItem drawerHomeItem=new PrimaryDrawerItem().withIdentifier(0).withName("Home").withSelectedBackgroundAnimated(true);
        PrimaryDrawerItem drawerItemPos=new PrimaryDrawerItem().withIdentifier(1).withName("POS").withSelectedBackgroundAnimated(true);
        PrimaryDrawerItem drawerItemInventory=new PrimaryDrawerItem().withIdentifier(2).withName("Inventory").withSelectedBackgroundAnimated(true);


        PrimaryDrawerItem drawerItemLogout=new PrimaryDrawerItem().withIdentifier(3).withName("Logout");

        AccountHeader header=new AccountHeaderBuilder().withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(usernname).withEmail(userEmail).withIcon(R.drawable.ic_launcher_foreground)
                ).withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                }).build();

        Drawer result=new DrawerBuilder()
                .withAccountHeader(header)
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)

                .addDrawerItems(
                        drawerHomeItem,
                        new DividerDrawerItem(),
                        drawerItemPos,
                        new DividerDrawerItem(),
                        drawerItemInventory,
                        new DividerDrawerItem(),
                        drawerItemLogout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(final View view, int position, IDrawerItem drawerItem) {
                        Intent intent=new Intent();

                        if(position==1){

                            intent=new Intent(activity, MainMenu.class);
                            view.getContext().startActivity(intent);

                        }
                        else if(position==3){
                            intent=new Intent(activity, PosActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        else if(position==5){
                            intent=new Intent(activity, Inventory.class);
                            view.getContext().startActivity(intent);

                        }
                        else if (position==7){
                            Client client=Common.client;
                            UserStore.logout(client, new KinveyClientCallback<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent=new Intent(activity,MainActivity.class);
                                    view.getContext().startActivity(intent);
                                }

                                @Override
                                public void onFailure(Throwable throwable) {

                                }
                            });
                        }

                        return false;
                    }
                }).build();


    }



}
