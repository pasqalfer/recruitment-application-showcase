import React from "react";
import Modal from 'react-modal';
import Icon from "./icons";
import {useUserContext} from "../context/context";

Modal.setAppElement('#root');

export default function CreatePlaylist({doCreatePlaylist}){
    const [modalIsOpen, setIsOpen] = React.useState(false);
    const [input,setInput] = React.useState();

    const customStyles = {
        content: {
            zIndex : 1,
            top: '50%',
            left: '15%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
        },
    };

    function openModal() {
        setIsOpen(true);
    }


    function closeModal() {
        setIsOpen(false);
    }

    return (
        <div>
            <button className="create-button no-outline" onClick={openModal}>
                <div className="playlist-icon">
                    <Icon name='Create' />
                </div>
                <span className="featured-label">Create Playlist</span>
            </button>
            <Modal
                isOpen={modalIsOpen}
                onAfterOpen={() => {}}
                onRequestClose={closeModal}
                style={customStyles}
                contentLabel="Example Modal"
            >
                <h2>{"Create Playlist"}</h2>
                <div>Enter playlist name</div>
                <form onSubmit={(e) => {
                    e.preventDefault()
                    doCreatePlaylist(e.target.playlistName.value)
                    closeModal()
                }}>
                    <input type="text" name="playlistName"/>

                    <button type="submit">Create</button>
                </form>


            </Modal>
        </div>
    );


}


export function AddToPlaylist({ doAddToPlaylist}){
    const [modalIsOpen, setIsOpen] = React.useState(false);
    const [input,setInput] = React.useState();
    const {user} = useUserContext();
    const customStyles = {

        content: {
            zIndex : 1,
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
        },
    };
    function openModal() {
        setIsOpen(true);
    }


    function closeModal() {
        setIsOpen(false);
    }

    return (<div>
            <button className="create-button no-outline" onClick={openModal}>
                <div className="playlist-icon">
                    <Icon name='Create' />
                </div>
            </button>
            <Modal
                isOpen={modalIsOpen}
                onAfterOpen={() => {}}
                onRequestClose={closeModal}
                style={customStyles}
                contentLabel="Example Modal"
            >
                <h2>{"Add Song to Playlist"}</h2>
                <div>Choose playlist : </div>
                <div className="other-playlist-container">
                    <ul className="other-list">
                        {user?.playlists.map((playlist,i) =>
                        {
                            return <div key={playlist.id+i} onClick={(e) => {
                                e.preventDefault();
                                doAddToPlaylist(playlist.id)
                                closeModal();}}>
                            <li className='side-list'>
                                <div className="list-wrapper">
                                    {playlist.name}
                                </div>
                            </li>
                        </div>
                        })}
                    </ul>
                </div>
            </Modal>
        </div>);
}