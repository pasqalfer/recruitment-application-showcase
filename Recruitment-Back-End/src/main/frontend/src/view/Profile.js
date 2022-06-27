import PageBanner from "./PageBanner";
import {Route} from "react-router-dom";
import CollectionRow from "./CollectionRow";
import RowTitle from "./RowTitle";
import TrackList from "./TrackList";
import React, {useState} from "react";
import TrackListItem from "./TrackListItem";


export function Profile({user,description, children}){
    const [bannerInfo, setbannerInfo] = useState({
        name: '',
        description: '',
        user: [],
        followers: 0,
        primary_color: 'rgb(83, 83, 83)',
        images: [],
        total: 0
    })

    React.useEffect(() => {
        setbannerInfo({name:user, description: description,...bannerInfo})
    }, [])

    return <>
        <div className='page-content' style={{paddingTop:'16px'}}>
            <PageBanner pageTitle='profile' bannerInfo={bannerInfo}/>
            {children}
        </div>

    </>
}

export function ProfilePlaylists({username, playlists = []}){

    return  <Profile user={username}>
        <CollectionRow name='Playlists' playlists={playlists}/>
    </Profile>
}

export function ProfileRecentlyPlayed({username,recentlyPlayed = []}){
    return  <Profile user={username}>
        <RowTitle title={'Recently Played'}/>
        <div className="playListContent">
            <div className="playListOverlay" style={{backgroundColor: 'rgb(83, 83, 83)'}}></div>
            <div className="page-content">
                <div className="trackListContainer">
                    <ol className="trackList">
                        {recentlyPlayed.map((track, index) => {

                                return <TrackListItem track={track.playedFile} key={track.id} />

                        })}
                    </ol>
                </div>
            </div>
        </div>
    </Profile>
}

export function ProfileUploads({username, uploads = []}){
    return <Profile user={username}>
        <RowTitle title={'Uploads'}/>
        <div className="playListContent">
            <div className="playListOverlay" style={{backgroundColor: 'rgb(83, 83, 83)'}}></div>
            <div className="page-content">
                <TrackList tracks={uploads}/>
            </div>
        </div>
    </Profile>

}