import React from 'react'
import TrackListItem from './TrackListItem'

function TrackList({tracks, styleName, highlight, playContextTrack}) {
    return (
        <div className="trackListContainer">
            <ol className="trackList">
                {tracks.map((track, index) => {
                    if (index+1 < tracks.length){
                        return <TrackListItem track={track} key={track.id} styleName={styleName} highlight={track.id === highlight}/>
                    }else{
                        return <TrackListItem track={track} key={track.id} styleName={styleName} highlight={track.id === highlight} />
                    }
                })}
            </ol>
        </div>
    )
}


export default TrackList