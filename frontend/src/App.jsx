import { useState } from 'react'
//import reactLogo from './assets/react.svg'
//import viteLogo from '/vite.svg'
//import './App.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';

import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import ProgressBar from 'react-bootstrap/ProgressBar';
import $ from 'jquery';
import ajaxForm from "jquery-form"

import download from "downloadjs"





function UploadApk() {

  const [showProgressBar, setShowProgressBar] = useState(false);
  const [errMsg, setErrMsg] = useState("");

  const [statusMsg, setStatusMsg] = useState("");

  async function onSubmit(e) {
    e.preventDefault();

    const formData = new FormData(e.target);

    setShowProgressBar(true);
    // clear previous err message 
    setErrMsg("");
    setStatusMsg("decompiling apk, please kindly wait :)");
    // make request
    const response = await fetch("http://localhost:8080/apk", {
      "referrer": "http://localhost:5173/",
      "referrerPolicy": "strict-origin-when-cross-origin",
      "method": "POST",
      "body": formData,
    });
    if (response.ok) {
      setShowProgressBar(false);
      setStatusMsg("");
      console.log("request is ok")

      const blob = await response.blob();
      download(blob, "source.zip")
    }
    else {
      setShowProgressBar(false);
      setStatusMsg("");
      setErrMsg("Input file is not valid, file must ends with .apk");
      console.log("request is not ok")

    }

  }



  return (
    <div >
      <iframe name="dummyframe" id="dummyframe" style={{ display: "none" }}></iframe>

      <div style={{ paddingBottom: "20px" }}>
        <Form method="POST" enctype="multipart/form-data" target="dummyframe" id="myForm"
          onSubmit={onSubmit}
        >
          <Form.Group controlId="formFile" className="mb-3" >
            <Form.Label>Choose apk file and press upload</Form.Label>
            <Form.Control type="file" name="file" />
          </Form.Group>

          <Button variant="primary" type="submit">
            Upload
          </Button>

        </Form>
      </div>
      <div>
        <span style={{ color: "red" }}>{errMsg}</span>
        <span>{statusMsg}</span>
        {
          showProgressBar && <ProgressBar animated now={100} />
        }

      </div>

    </div>
  )
}

function ProjectDescription() {
  return (
    <div>
      Uncover the hidden code within your favorite closed-source apps with
      This website, which harnesses the power of JADX, an
      open-source library, to transform APK files into easily readable and
      editable Java code.
    </div>
  )
}
function App() {
  const [count, setCount] = useState(0)
  document.documentElement.setAttribute("data-bs-theme", "dark")

  return (
    <>
      <div style={{ padding: "20px" }}>
        <h1> Online Apk Decompiler</h1>
        <Container>

          <Row>
            <UploadApk></UploadApk>
            <div style={{ paddingTop: "20px" }}>
              <ProjectDescription></ProjectDescription>
            </div>

          </Row>

        </Container>
      </div>
    </>
  )
}

export default App
