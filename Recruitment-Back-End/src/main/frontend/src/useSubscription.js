import React from "react";
import * as SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";

export default function useSubscription({args, destination, onChange}) {
    React.useEffect(() => {
        if(args && args.username || args && args.playlist){
            return connect({args,destination,onChange})
        }
    },[])


}

export     function connect({args, destination, onChange}){
    let socket = SockJS("http://localhost:8080/subscribe")
    let stompClient = (Stomp.over(() => socket))
    stompClient.connect(args, function() {
        console.log('Web Socket is connected');
        let subscription = stompClient.subscribe(destination, function(message) {
            onChange(JSON.parse(message.body))
        });
    });
    return () => stompClient.disconnect()
}

