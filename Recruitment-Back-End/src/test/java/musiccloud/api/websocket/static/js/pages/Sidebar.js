
function Sidebar({username, playlists}) {
    return (
        <>
            <div className="sidebar">
                <NavList>
                    <NavItem name={"search"} to={"#/search"} label={"Search"}/>
                    <NavItem name={"user"} to={"#/profile?name="+username} label={"Search"}/>
                </NavList>

               <Playlists playlists={playlists}/>

            </div>
        </>
    );


}

export default Sidebar;
