import React, {createContext} from 'react'
import {connect} from "../useSubscription";
import APIUtil from "../util/APIUtil";

export const UserContext = createContext({})
export const PlayerContext = createContext(() => {})


export function UserProvider({user,token, children}){
    const [currentUser, setCurrentUser] = React.useState(undefined);
    const [_token, _setToken] = React.useState()
    const [loading,setLoading] = React.useState()
    const [blob,setBlob] = React.useState()
    const [play,setPlay] = React.useState(false);

    React.useEffect(() => {
        _setToken(token)
        if(user.username){
            return connect(   {
                args: {username : user.username},
                destination: '/users/topic/profile',
                onChange: (m) => {
                    setCurrentUser(m)
                }
            })
        }
    }, [])

    function addPlaylist(name){
        let headers = defaultHeader();
        headers['Content-Type'] = 'application/json'
        return fetch('/api/user/playlists/add', {
            headers:headers,
            method: "POST",
            body: JSON.stringify({
                name: name,
                shareable: true
            })}).then(res => {
            if(res.ok)
                return res.json()
        }).catch(reason => {
            console.log(reason)
        });
    }

    function addToPlaylist(playlistId,audioId){
        let headers = defaultHeader();
        headers['Content-Type'] = 'application/json'
            const playlists =  fetch('/api/user/playlists/add-song', {
                headers: headers,
                method: "POST",
                body: JSON.stringify({
                    playlistId: playlistId,
                    audioId: audioId
                })}).then(res => {
                if(res.ok)
                    return res.json()
            }).catch(reason => {
                console.log(reason)
            });

    }

    function uploadSong(audioFile){
        let headers = defaultHeader()
        let formData  = new FormData();
        formData.append("audioFile", audioFile)
        // headers['Content-Type'] = 'application/json'
        return fetch('/api/user/audio/upload', {
            headers:headers,
            method: "POST",
            body: formData

        }).then(res => {
            console.log(res)
        }).catch(reason => {
            console.log(reason)
        });
    }

    function downloadSong(id){
        let headers = defaultHeader()
        return fetch('/api/user/audio/'+id+'/play', {
            headers:headers,
            method: "GET",
        }).then(res => {
            if(res.ok)
               return res.blob()
        }).then(r => {
                setBlob(URL.createObjectURL(r))
                setPlay(true)

            }
        )
            .catch(reason => {
            console.log(reason)
        });

    }

    function searchUser(query=''){
        let headers = defaultHeader();
        headers['Content-Type'] = 'application/json'
        headers['Accept'] = 'application/json'
        return  fetch('/api/search/user?name=' + query,{
            headers:headers,
            method: "GET",
        })
            .then(res => res.json())
    }

    function defaultHeader() {
        let headers = {}
        headers[_token.headerName] = _token.token
        return headers;
    }

    const actions = {addPlaylist, uploadSong,addToPlaylist,searchUser,downloadSong, setPlay}

    return <UserContext.Provider value={{user:currentUser, actions, song:blob, playing:play}}>
        {children}
    </UserContext.Provider>
}

export function useUserContext(){
    return React.useContext(UserContext)
}

export const LoginContext = createContext(false)
export const TokenContext = createContext(null)
export const MessageContext = createContext(() => {})
export const PlayContext = createContext(() => {})

