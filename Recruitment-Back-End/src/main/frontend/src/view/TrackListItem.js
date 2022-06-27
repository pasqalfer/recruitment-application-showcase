import React, { useContext } from "react";
import Icon from './icons'
import {AddToPlaylist} from "./CreatePlaylist";
import {useUserContext} from "../context/context";



function TrackListItem({ track, styleName, highlight })  {
	const { id, album, artist, name,title, explicit, duration_ms, uri } = track;
	const {actions} = useUserContext();

	let thumbNail;

	return (
		<li
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
						actions.downloadSong(id)
					}}
				>
					<Icon name="Play" height="20" width="20" />
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
								<Icon name="Music2" />
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
					<div className="trackName ellipsis-one-line">{title}</div>

					{styleName !== "simplify" && (
						<div className="trackInfo">
								<span
									className="explicit-label"
									style={explicit ? { display: "flex" } : { display: "none" }}
								>
									E
								</span>

							{album && (
								<>
									<span className="trackInfoSep">•</span>
									<span className="trackAlbum ellipsis-one-line">
										{artist}-{album}
									</span>
								</>
							)}
						</div>
					)}
				</div>
			</div>

			<div className="trackItemDuration">
				<AddToPlaylist doAddToPlaylist={(playlistID) => {actions.addToPlaylist(playlistID,track.id)}}/>
			</div>
		</li>
	);
}

const simplyStyle = {
	display: "flex",
	alignItems: "center",
	justifyContent: "flex-end",
};

export default TrackListItem;
