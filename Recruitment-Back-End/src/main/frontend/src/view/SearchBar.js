import React, {useEffect, useState} from 'react'
import Icon from './icons'

export default function SearchBar({submit}) {
    const [query, setQuery] = useState('')

    return (
        <div className="SearchContainer">
            <div className='SearchBar'>
                <div style={iconStyle}>
                    <Icon name="N-Search" /> 
                </div>
                <form onSubmit={e => {
                    e.preventDefault();
                    submit(query);
                }}>
                    <input className= 'SearchInput no-outline'
                           maxLength='80'
                           autoCorrect='off'
                           autoCapitalize='off'
                           spellCheck='false'
                           autoFocus={true}
                           placeholder='Search for Users Songs, or Playlists'
                           value={query}
                           onChange={e => setQuery(e.target.value)}/>

                </form>

            </div>
        </div>
    )
}


const iconStyle = {
    position:'absolute',
    top: '0',
    bottom: '0',
    left: '12px',
    display: 'flex',
    alignItems: 'center',
    cursor:'text'
}

