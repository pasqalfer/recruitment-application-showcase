function Playlists({playlists}) {
    return  <div className='playlists'>
        <h1 className='play-title'>playlists</h1>
        <div className="featured-playlists">
            <button className="create-button no-outline" onClick={() => alert('Oops, it look like I chose not to implement this feature :)')}>
                <div className="playlist-icon">
                    +
                </div>
                <span className="featured-label">Create Playlist</span>
            </button>
        </div>
        <hr className="list-separator"/>
        <div className="other-playlist-container">
            <ul className="other-list">
                {playlists.map((playlist) => (
                        <li className='side-list'>
                            <a to={`#/playlist?playlist=`+playlist.id} className='list-link'>
                                <div className="list-wrapper">
                                    {playlist.name}
                                </div>
                            </a>
                        </li>
                    )
                    )}
            </ul>
        </div>
    </div>
}