

class UserProfile{

    constructor(username) {
        this.username = username;
        this.subscribers = [];
        this.playlists = [];
        this.uploads = [];
        this.recentlyListened = [];
        this.connected = false;
        this.tab = 0;
        this.disconnect = () => {};
    }

    addSubscriber(callback){
        this.subscribers.push(callback)
        return this;
    }

    setTab(tab){
        const temp = this;
        this.tab = tab;
        this.notifySubscribers(temp,this)
    }

    doConnect(){
        this.disconnect()
        this.disconnect = connect({
            args : {username : user},
            destination: '/users/topic/profile',
            onChange: (m) => {
                this.connected = true;
                this.username = m.username;
                this.playlists = m.playlists;
                this.uploads = m.userUploads;
                this.notifySubscribers(m)
            }
        })
        return this;
    }

    notifySubscribers(m){
        this.subscribers.forEach(cb => {
            cb(this, m)
        })
    }
}

class ConnectedModel{
    constructor(connectFunction) {

        connect()
    }
}