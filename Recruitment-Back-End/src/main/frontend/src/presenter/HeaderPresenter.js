import {Route} from "react-router-dom";
import CollectionNav from "../view/CollectionNav";
import React, {useState} from "react";

export default function HeaderPresenter(){
    const [query, setQuery] = useState('')

    const resetQuery = ()=>{
        setQuery('')
    }

    return <>
        <Route path='/profile/:name'>
            <CollectionNav/>
        </Route>
    </>
}