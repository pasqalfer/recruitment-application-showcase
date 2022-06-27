import React, {useState} from "react";
import {useUserContext} from "../../context/context";
import {connect} from "../../useSubscription";
import NowPlaying from "../NowPlaying";

function Player({user}){
	const {playing, setPlay} = useUserContext();
	const [url,setURL] = React.useState('')
	const [currentSong, setCurrentSong] = React.useState();

	React.useEffect(() => {
		debugger
		if(user){
			return connect(   {
				args: {username : user},
				destination: '/users/topic/player',
				onChange: (m) => {
					setCurrentSong(m)
					document.getElementById("player").src=m.url
					console.log(m)
				}
			})
		}

	}, [])

	return 	<div className="player">
		<div className="player-left">
			{currentSong && <NowPlaying playInfo={currentSong.metadata}/>}
		</div>

		<div className="player-center">
			<audio id="player" controls autoPlay={playing}>
			</audio>
		</div>

		<div className="player-right">

		</div>
	</div>

}

export default Player;
