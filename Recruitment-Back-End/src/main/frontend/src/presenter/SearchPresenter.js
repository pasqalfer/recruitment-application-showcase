import PageTitle from "../view/PageTitle";
import React, {useState} from "react";
import SearchBar from "../view/SearchBar";
import SearchRowTitle from "../view/SearchRowTitle";
import {Link, NavLink} from "react-router-dom";
import CardDisplay from "../view/CardDisplay";
import CardInfo from "../view/CardInfo";
import Icon from "../view/icons";
import {useUserContext} from "../context/context";


export default function SearchPresenter(){
    const {actions} = useUserContext();
    const [query, setQuery] = useState('')
    const [tab,setTab] = useState(0)
    const [usersResult, setUsersResult] = React.useState([])

    const resetQuery = ()=>{
        setQuery('')
    }

    const search = (query) => {
        actions.searchUser(query).then(r => {
            setUsersResult(r)
        })
    }

    console.log(usersResult)

    return <>
        <div className="page-content">
            <div className='browsePage'>
                <PageTitle name='Search' />
                <SearchBar submit={search}/>

                {tab === 0 ?  <div className='CollectionRow' style={{display: usersResult.length===0? 'none':'grid'}}>
                    <SearchRowTitle title={'Songs'}/>
                    <div className="RowGrid">
                        {usersResult.map((item) => {
                            return <ProfileCard key={item.id} info={item} />
                        })}
                    </div>
                </div>  : null}

            </div>
        </div>
    </>


}

 function SearchResult({result}){
    console.log(result)
    return <div className="holder">
        {result.map((e,i) => {
            return  <div onClick={() => {
            }}> <ProfileCard key={e.id} name={e.username} /> </div>

        })}
    </div>
}


function ProfileCard({info}){
    console.log(info)
    const image_url = []
    return (
        <div className='pcWrapper'>
            <Link to={ `/profile/${info.username}/`}>
                <div className="PlayCard">
                    <CardDisplay url={image_url}/>
                    <CardInfo title={info.username}/>
                </div>
            </Link>
        </div>
    )
}