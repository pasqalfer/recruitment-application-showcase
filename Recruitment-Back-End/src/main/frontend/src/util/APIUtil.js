import * as SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
export const playlistDest = '/users/topic/playlist'
export const userDest = '/users/topic/profile'

class APIUtil{

    constructor({headerName, token}) {
        this.headerName = headerName;
        this.token = token;
    }

    addPlaylist (playlistName,sharable) {
        debugger;
        let headers = {}
        headers[this.headerName] = this.token
        headers['Content-Type'] = 'application/json'
        return fetch('/api/user/playlists/add', {
            headers:headers,
            method: "POST",
            body: JSON.stringify({
                name: playlistName,
                shareable: sharable
            })}).then(res => {
            if(res.ok)
                return res.json()
        }).catch(reason => {
            console.log(reason)
        });
    }

    addSongToList =  (playlistId,audioId) => {
        let headers = {}
        headers[this.headerName] = this.token
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

    searchUser(query=''){
        let headers = {}
        headers[this.headerName] = this.token
        headers['Content-Type'] = 'application/json'
        headers['Accept'] = 'application/json'
        return  fetch('/api/search/user?name=' + query,{
            headers:headers,
            method: "GET",
        })
            .then(res => res.json())
    }

    subscribeTo(destination, args, onChangeCallback){
        let socket = SockJS("http://localhost:8080/subscribe")
        let stompClient = Stomp.over(() => socket)
        stompClient.connect(args, function() {
            console.log('Web Socket is connected');
            let subscription = stompClient.subscribe(destination, function(message) {
                onChangeCallback(JSON.parse(message.body))
            });
        });
        return () => stompClient.disconnect()
    }

    uploadSong(audioFile){
        let headers = {}
        headers[this.headerName] = this.token
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

    downloadSong(id){
        let headers = {}
        headers[this.headerName] = this.token
        return fetch('/api/user/audio/'+id+'/play', {
            headers:headers,
            method: "GET",
        }).then(res => {
            if(res.ok)
                return res.blob()
        }).catch(reason => {
            console.log(reason)
        });
    }

}

export default APIUtil