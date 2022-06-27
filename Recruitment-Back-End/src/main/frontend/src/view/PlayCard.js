import React, {useContext} from 'react'
import {useHistory, Link} from 'react-router-dom'

import CardInfo from './CardInfo'
import CardDisplay from './CardDisplay'
import Icon from './icons'
const PlayCard = React.forwardRef(({info, type}, ref) => {
    console.log(info)
    const image_url = []
    return (
        <div className='pcWrapper'>
            <Link to={ `/playlist/${info.id}`}>
                <div ref={ref} className="PlayCard">
                    <CardDisplay url={image_url} type={type}/>
                    <CardInfo title={info.name}/>
                </div>
            </Link>

            <button className="smallButton no-outline" title="Play" onClick={() => {}}>
                <Icon name="Play" height='17' width='17'/>
            </button>

        </div>
    )
})


function returnDescription(type, info){
    let artists
    switch (type){
        case 'playlist':
           return info.description || `By ${info.owner.display_name}`
        case 'album':
            artists = info.artists.map((object) => object.name)
            return artists.length === 1 ? artists[0]:artists.join(', ')
        case 'artist':
            return 'artist'
        case 'track':
            artists = info.artists.map((object) => object.name)
            return artists.length === 1 ? artists[0]:artists.join(', ')
        default:
            return null
    }
}


export default PlayCard