import React, {Component} from 'react';
import EditorWyswig from '../components/EditorWyswig';

export default class EditorArticles extends Component{
    render() {
        return (
            <div > EditorArticles 
                TITLE <input></input>
                <EditorWyswig />
            </div>
        )
    }
}