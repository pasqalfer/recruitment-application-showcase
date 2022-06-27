

function connect(destination, args, onChange){
    let socket = new SockJS("http://localhost:8080/subscribe")
    let stompClient = (Stomp.over(socket))

    stompClient.connect(args, function() {
        console.log('Web Socket is connected');
        let subscription = stompClient.subscribe(destination, function(message) {
            onChange(JSON.parse(message.body))
        });
    });
    return () => stompClient.disconnect()
}



function populateSidebarPlaylists(playlists){
    let $currentUserPlaylists = $('#current-user-playlists')
    $currentUserPlaylists.html('')
    playlists.map(playlist => {
        $currentUserPlaylists.append(
            $('<li>')
                .addClass("nav-item")
                .append($("<a>")
                    .attr("href", "/playlist/"+playlist.id)
                    .text(playlist.name)
                    .addClass("nav-link")
                    .append('<i data-feather="music"></i>')))
    })

}