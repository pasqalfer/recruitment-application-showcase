import React,{useState} from "react";
import {useUserContext} from "../context/context";
import {connect} from "../useSubscription";
import {Route, useParams} from "react-router-dom";
import {Profile, ProfilePlaylists, ProfileRecentlyPlayed, ProfileUploads} from "../view/Profile";
import NowPlaying from "../view/NowPlaying";


export default function ProfilePresenter(){
    const [profile, setProfile] = useState([])
    const [currentlyPlaying,setCurrentlyPlaying] = useState({});
    const {user,actions} = useUserContext();
    const {name} = useParams()

    React.useEffect(() => {
        let disconnect1 = connect({
            args : {username: name},
            destination : '/users/topic/profile',
            onChange: (m) => {
                setProfile(m)
            }
        })

        let disconnect2 = connect(   {
            args: {username : user},
            destination: '/users/topic/player',
            onChange: (m) => {
                setCurrentlyPlaying(m.metadata)
            }
        })

        return () => {
            disconnect1();
            disconnect2();
        }

    }, [])

    console.log(profile)

    return <>
        <Route path='/profile/:name/uploads'>
            <ProfileUploads username={profile.username} uploads={profile.userUploads}/>
        </Route>
        <Route  path='/profile/:name/playlist'>
            <ProfilePlaylists name='Playlists' username={profile.username} playlists={profile.playlists}/>
        </Route>
        <Route path='/profile/:name/news'>
            <ProfileRecentlyPlayed name='Playlists'username={profile.username} recentlyPlayed={profile.recentlyPlayedFeeds} />
        </Route>
    </>
}
