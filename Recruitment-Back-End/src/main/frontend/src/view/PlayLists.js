import React from 'react';
import ListItem from "./ListItem";
import CreatePlaylist from "./CreatePlaylist";

function PlayLists({playlists = [], doCreatePlaylist}){
    return (
        <div className='playlists'>
            <h1 className='play-title'>playlists</h1>
            <div className="featured-playlists">
                <>
                    <CreatePlaylist doCreatePlaylist={doCreatePlaylist}/>
                </>
            </div>
            <hr className="list-separator"/>
            <div className="other-playlist-container">
                <ul className="other-list">
                    {playlists.map((playlist) => <ListItem key={playlist.id} name={playlist.name} id={playlist.id}/>)}
                </ul>
            </div>        </div>
    );
}

export default PlayLists;

