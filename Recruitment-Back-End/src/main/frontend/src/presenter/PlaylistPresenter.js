import {useParams} from "react-router-dom";
import React from "react";
import {connect} from "../useSubscription";
import Playlist from "../view/Playlist";


export default function PlaylistPresenter(){
    const [playlist, setPlaylist] = React.useState()
    const {id} = useParams()

    React.useEffect(() => {
        if(id){
            return connect(   {
                args: {playlist : id},
                destination: '/users/topic/playlist',
                onChange: (m) => {
                    setPlaylist(m)
                }
            })
        }
    }, [id])

    console.log(playlist)
    return !playlist && <div>...</div> || <Playlist name={playlist.name} tracks={playlist.playlistSongs}/>
}