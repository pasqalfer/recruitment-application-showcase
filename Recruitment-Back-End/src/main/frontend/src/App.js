import React, {useRef} from 'react';
import Footer from "./view/footer/Footer";
import Player from "./view/footer/Player";
import Loading from "./view/Loading";

import {useUserContext} from "./context/context";
import SidebarPresenter from "./presenter/SidebarPresenter";
import {Route, Switch} from "react-router-dom";
import ProfilePresenter from "./presenter/ProfilePresenter";
import HeaderPresenter from "./presenter/HeaderPresenter";
import PlaylistPresenter from "./presenter/PlaylistPresenter";
import SearchPresenter from "./presenter/SearchPresenter";
import {connect} from "./useSubscription";

function App() {
    const {user,actions} = useUserContext();
    const playerRef = useRef(null)



  return (
    <div className="App">
      {!user?
        <Loading type='app'/> :
          <React.Fragment>
             <SidebarPresenter/>
              <div className='featured'>
                  <HeaderPresenter/>
                  <Switch>

                      <Route exact path='/'>
                          <form onSubmit={(e) => {
                              e.preventDefault();
                              actions.uploadSong(e.target.audioFile.files[0])
                          } }>
                              <input type="file" name="audioFile"/>
                              <button type="submit">submit</button>
                          </form>
                      </Route>
                      <Route exact path='/profile/:name/*'>
                      <ProfilePresenter></ProfilePresenter>
                  </Route>
                  <Route path={`/search`}>
                      <SearchPresenter/>
                  </Route>
                      <Route exact={false} path='/playlist/:id'>
                          <PlaylistPresenter></PlaylistPresenter>
                      </Route>
              </Switch>

              </div>
              <Footer>
                    <Player user={user.username}></Player>
              </Footer>
          </React.Fragment>

      }
    </div>
  );
}



export default App;
