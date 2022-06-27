import Sidebar from "../view/Sidebar";
import Logo from "../view/Logo";
import NavItem from "../view/NavItem";
import NavList from "../view/NavList";
import PlayLists from "../view/PlayLists";
import React from "react";
import {useUserContext} from "../context/context";

export default function SidebarPresenter(){
    const {user,actions} = useUserContext();
    const {username, playlists} = user;

    return  <Sidebar>
        <Logo/>
        <NavList>
            <NavItem to='/' exact={true} name='Home' label='Home' />
            <NavItem to='/search' exact={true} name='Search' label='Search' />
            <NavItem to={'/profile/'+username} exact={false} name='Library' label='Your Library' data_tip='library' data_for='tooltip' data_event='click'/>
        </NavList>
        <PlayLists playlists={playlists} doCreatePlaylist={actions.addPlaylist}/>
    </Sidebar>
}