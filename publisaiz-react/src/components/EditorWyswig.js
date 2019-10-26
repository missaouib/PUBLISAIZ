
// installed : npm install -S react-draft-wysiwyg
// https://jpuri.github.io/react-draft-wysiwyg/#/docs?_k=jjqinp

import React, { Component } from 'react';
import { EditorState } from 'draft-js';
import { Editor } from 'react-draft-wysiwyg';
import 'react-draft-wysiwyg/dist/react-draft-wysiwyg.css';


export default class EditorWyswig extends Component {
  constructor(props) {
    super(props);
    this.state = {
      editorState: EditorState.createEmpty(),
    };
  }

  onEditorStateChange = (editorState) => {
    this.setState({
      editorState,
    });
  };

  render() {
    const { editorState } = this.state;
    return (
        <div>
        <Editor 
            editorState={editorState} 
            wrapperClassName="demo-wrapper"
            editorClassName="demo-editor" 
            onEditorStateChange={this.onEditorStateChange} 
        />

        </div>
    )
  }
}