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
  async function onSubmit(e) {
    e.preventDefault();

    const formData = new FormData(e.target);
    const data = new URLSearchParams(formData);
    /*

    fetch("http://localhost:8080/apk", {
      method: "POST",
      body: formData,
      mode: "no-cors"

    }).then(function (response) {
      console.log(response.json());
    }).catch(function (error) {
      console.log('Request failed', error)
    });;
    */

    const response = await fetch("http://localhost:8080/apk", {
      "headers": {
        "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
        "accept-language": "en-US,en;q=0.9",
        "cache-control": "max-age=0",
        "sec-ch-ua": "\"Not/A)Brand\";v=\"99\", \"Brave\";v=\"115\", \"Chromium\";v=\"115\"",
        "sec-ch-ua-mobile": "?0",
        "sec-ch-ua-platform": "\"Linux\"",
        "sec-fetch-dest": "iframe",
        "sec-fetch-mode": "navigate",
        "sec-fetch-site": "same-site",
        "sec-fetch-user": "?1",
        "sec-gpc": "1",
        "upgrade-insecure-requests": "1"
      },
      "referrer": "http://localhost:5173/",
      "referrerPolicy": "strict-origin-when-cross-origin",
      "method": "POST",
      "body": formData,
    });
    if (response.ok) {
      console.log("request is ok")

      const blob = await response.blob();
      download(blob, "source.zip")
    }
    else {
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
      <ProgressBar animated now={100} />
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
