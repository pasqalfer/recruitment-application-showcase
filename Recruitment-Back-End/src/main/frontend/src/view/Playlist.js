import PageBanner from "./PageBanner";
import TrackList from "./TrackList";
import React, {useContext, useState} from "react";
import Icon from "./icons";


export default function Playlist({name, tracks}){
    const [bannerInfo, setbannerInfo] = useState({
        name: '',
        description: '',
        user: [],
        followers: 0,
        primary_color: '#262626',
        images: [],
    })

    React.useEffect(() => {
        setbannerInfo({name:name,...bannerInfo})
    }, [])

    return  <div className='listPage' style={{display: 'block'}}>
        <PageBanner pageTitle='playlist' bannerInfo={bannerInfo}/>
        <div className="playListContent">
            <div className="playListOverlay" style={{backgroundColor: `${bannerInfo.primary_color}`}}></div>
            <button className="playButton no-outline" title="Play" onClick={() => {}}>
                <Icon name="Play" height='28' width='28'/>
            </button>
            <div className="page-content">
                <TrackList tracks={tracks}/>
            </div>

        </div>
    </div>
}


function PlayButtonLarge(){

        return (
            <button className="playButton no-outline" title="Play" onClick={() => {}}>
                <Icon name="Play" height='28' width='28'/>
            </button>
        )

}