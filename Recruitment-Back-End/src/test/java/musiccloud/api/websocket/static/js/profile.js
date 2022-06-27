
function showTab(tabs,tabIndex){
    tabs.map((tabId,i) => {
        if(i == tabIndex){
            $(tab.id).show()
        } else {
            $(tab.id).hide()
        }
    })
}

function showTab1(){
    $('#tabs-1').show()
    $('#tabs-2').hide()
    $('#tabs-3').hide()
}

function showTab2(){
    $('#tabs-1').hide()
    $('#tabs-2').show()
    $('#tabs-3').hide()
}

function showTab3(){
    $('#tabs-1').hide()
    $('#tabs-2').hide()
    $('#tabs-3').show()
}

function populateProfileUploads(songs){
    let songList = $('#userUploadsList')
    songList.html('')
    songs.forEach(song => {
        $('<tr>')
            .append($("<td>").append('<i data-feather="play"></i>'))
            .append($("<td>").text(song.title))
            .append($("<td>").text(song.artist))
            .append($("<td>").text(song.album))
            .append($("<td>").append($("<button>").attr("id", song.id).text("+").addClass("btn-add-to-playlist")))
            .appendTo(songList)
    })
    feather.replace()
}

function appendAddToPlaylistDropdown(playlists){
    $(function() {
        let $contextMenu = $("#contextMenu");
        $contextMenu.html('')
        let $ul = $('<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="display:block;position:static;margin-bottom:5px;">')

        playlists.map(p => {
            $ul.append($('<li>').text(p.name).attr("id", p.id))
        })

        $contextMenu.append($ul)

        $(".btn-add-to-playlist").click(function(e) {
            $contextMenu.css({
                display: "block",
                left: e.pageX,
                top: e.pageY
            });

            $contextMenu.attr("currentSongId", e.target.id)
            return false;
        });

        $('html').click(function() {
            $contextMenu.hide();
        });

        $("#contextMenu li").click(function(e){
            let  f = $(this);
            let playlistID  = f.attr("id");
            let songID = $contextMenu.attr("currentSongId");
            apiUtil.addSongToList(playlistID,songID)
        });

    });
}

function populateProfilePlaylists(playlists){
    let songList = $('#userPlaylists')
    songList.html('')
    playlists.forEach(playlist => {
        $('<tr>')
            .append($("<td>").append($("<a>").attr("href", "/playlist/"+playlist.id).append('<i data-feather="play"></i>')))
            .append($("<td>").text(playlist.name))
            .append($("<td>").text(playlist.playlistSongs.length))
            .appendTo(songList)
    })
    feather.replace()
}


function renderProfile() {
    document.getElementById("root").innerHTML = 'loading...'

    apiUtil.fetchPage(location.hash.replace('#','')).then((r) => {
        let temp = $.parseHTML(r)
        console.log(temp)

        $('#root').append(temp)
    })
    //Do stuff--e.g. get via AJAX -> render template (optional) -> append HTML to an element
}