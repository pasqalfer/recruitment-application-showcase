


const ProfilePage = ({username, playlists, tab,setTab, uploads}) => {

    return <React.Fragment>
        <div className='featured'>
            <Navbar>
                <div className='cNavWrapper'>
                    <nav className='cNav'>
                        <ul className='cNavList'>
                            <li onClick={() => setTab(0)}>
                                Playlists
                            </li>
                            <li onClick={() => setTab(1)}>
                                Uploads
                            </li>
                        </ul>
                    </nav>
                </div>
            </Navbar>
            {tab == 0 &&   <React.Fragment>
                <RowTitle title={'Playlists'}/>
                <div className="playListContent">
                    <div className="page-content">
                        <TrackList tracks={playlists}/>
                    </div>
                </div>
            </React.Fragment>}

            {tab == 1 &&   <React.Fragment>
                <RowTitle title={'Uploads'}/>
                <div className="playListContent">
                    <div className="page-content">
                    </div>
                </div>
            </React.Fragment>}

        </div>
    </React.Fragment>

}

function RowTitle({title, id}) {
    return (
        <div className="RowTitle">
            <h1 style={{fontSize:'24px',
                lineHeight:'28px',
                letterSpacing: '-0.04em',
                fontWeight: '700',
                color:'white'}}>{title}</h1>
            {id? <a href={`/genre/${id}`} className='seeAll'>see all</a>:null}
        </div>
    )
}


function TracksList({tracks}){
    return    <div className="trackListContainer">
        <ol className="trackList">
            {tracks.map((track, index) => {
                return <TrackListItem key={track.id} name={track.name}></TrackListItem>
            })}
        </ol>
    </div>
}

const TrackListItem = React.forwardRef(
    ({ track, styleName, highlight }, ref) => {
        const {  name } = track;

        let thumbNail;
        if (styleName === "simplify" && album.images.length > 0) {
            thumbNail = album.images[album.images.length - 1].url;
        }
        return (
            <li
                ref={ref}
                className={`trackListItem ${highlight ? "highlight" : null}`}
            >
                <div
                    className="trackItemPlay"
                    style={styleName === "simplify" ? simplyStyle : null}
                >
                    <button
                        className={
                            styleName === "simplify"
                                ? "hoverIcon no-outline"
                                : "hoverIcon trackTopAlign no-outline"
                        }
                        onClick={() => {

                        }}
                    >
                        <i name="play"/>
                    </button>
                    <div
                        className={
                            styleName === "simplify" ? "itemIcon" : "itemIcon trackTopAlign"
                        }
                        style={{ marginTop: styleName === "simplify" ? "0" : null }}
                    >
                        <Icon name="Music" />
                    </div>
                </div>

                {styleName === "simplify" && (
                    <div className="trackMidAlign">
                        <div className="trackItemThumb">
                            {thumbNail ? (
                                <img
                                    loading="lazy"
                                    src={thumbNail}
                                    style={{ width: "100%", height: "100%" }}
                                    alt=""
                                />
                            ) : (
                                <div
                                    style={{
                                        position: "absolute",
                                        top: "35%",
                                        bottom: "35%",
                                        left: "35%",
                                        right: "35%",
                                    }}
                                >
                                    <i name="music" />
                                </div>
                            )}
                        </div>
                    </div>
                )}

                <div className="trackItemInfo">
                    <div
                        className={
                            styleName === "simplify" ? "trackMidAlign" : "trackTopAlign"
                        }
                    >
                        <div className="trackName ellipsis-one-line">{name}</div>

                        {styleName !== "simplify" && (
                            <div className="trackInfo">


                            </div>
                        )}
                    </div>
                </div>

                <div className="trackItemDuration">
                    <div
                        className={`duration ${
                            styleName === "simplify" ? "trackMidAlign" : "trackTopAlign"
                        }`}
                    >
                        <span>{formattedTime}</span>
                    </div>
                </div>
            </li>
        );
    }
);

const simplyStyle = {
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-end",
};