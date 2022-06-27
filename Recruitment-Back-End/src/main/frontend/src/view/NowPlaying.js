import React from 'react'
import Icon from "./icons";
import {useUserContext} from "../context/context";


export default function NowPlaying({playInfo}) {
    const {artist, title, id} = playInfo
    let imageUrl;

    const {downloadSong} = useUserContext();


    return (
    <div className="now-playing">
            <div className="player-cover" onClick={() => {
                downloadSong(id)
            }}>

            </div>
                <div className="player-info-track ellipsis-one-line">
                    <p>{title}</p>
                </div>
        </div>
    )
}
