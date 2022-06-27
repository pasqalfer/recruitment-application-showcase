import React from 'react';
import {NavLink, useParams} from 'react-router-dom'

const CollectionNav = () => {
    const {name} = useParams();

    return (
        <div className='cNavWrapper'>
            <nav className='cNav'>
                <ul className='cNavList'>
                    <li>
                        <NavLink to={'/profile/' + name + '/playlist'} activeStyle={activeStyle}>Playlists</NavLink>
                    </li>
                    <li>
                        <NavLink  to={'/profile/' + name + '/uploads'} activeStyle={activeStyle}>Uploads</NavLink>
                    </li>
                    <li>
                        <NavLink to={'/profile/' + name + '/news'} activeStyle={activeStyle}>Recently Played</NavLink>
                    </li>
                </ul>
            </nav>
        </div>
    );
}

const activeStyle = {
    backgroundColor: '#333'
}

export default CollectionNav;
