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
import md5 from "js-md5"

import download from "downloadjs"

const BASE_API_URL = "http://localhost:8080"

const APK_DECOMPILE_API_URL = [BASE_API_URL, "apk"].join("/")

const APK_INFO_API_URL = [APK_DECOMPILE_API_URL, "info"].join("/")

const METHODS_SHOWN_LIMIT = 1000;



async function getApkClassInfos(apkHash, onGetApkInfo) {
  const requestUrl = [APK_INFO_API_URL, apkHash].join("/")

  const response = await fetch(requestUrl, {
    "method": "get",
  }
  );

  if (response.ok) {
    // console.log("request is ok")
    const responseBody = await response.json();
    onGetApkInfo(responseBody);

    _methodShownItem = []
    for (let i = 0; i < responseBody.length; i++) {
      const javaClass = responseBody[i]
      const javaClassName = javaClass["className"]
      const methods = javaClass["methodStrings"]

      _methodShownItem.push(
        <div>
          <h3> {javaClassName}</h3>
          {
            methods.map(method =>
              <li>{method}</li>
            )

          }

        </div>
      );
    }
    setMethodShownItem(_methodShownItem)
  }

}

function UploadApk({ onGetApkInfo }) {

  const [showProgressBar, setShowProgressBar] = useState(false);
  const [errMsg, setErrMsg] = useState("");

  const [statusMsg, setStatusMsg] = useState("");

  const [apkHash, setApkHash] = useState("");

  async function onSubmit(e) {
    e.preventDefault();

    const formData = new FormData(e.target);

    setShowProgressBar(true);
    // clear previous err message  and hash message
    setErrMsg("");
    setApkHash("");
    setStatusMsg("decompiling apk, please kindly wait :)");
    // make request
    const response = await fetch(APK_DECOMPILE_API_URL, {
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
      const currentApkHash = response.headers.get("Apkhash")
      setApkHash(currentApkHash);

      getApkClassInfos(currentApkHash, onGetApkInfo)

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

        {
          apkHash !== "" ? (
            <span>
              md5: <span style={{ color: "green" }}>{apkHash}</span>
            </span>
          ) :
            (<div></div>)
        }

      </div>

    </div >
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
  const [methodShownItem, setMethodShownItem] = useState([]);
  document.documentElement.setAttribute("data-bs-theme", "dark")

  return (
    <>
      <div style={{ padding: "20px" }}>
        <h1> Online Apk Decompiler</h1>
        <Container>

          <Row>
            <UploadApk onGetApkInfo={
              function (responseBody) {

                const _methodShownItem = []
                // set some limit
                const shownMethodLength = Math.min(responseBody.length, METHODS_SHOWN_LIMIT);
                for (let i = 0; i < shownMethodLength; i++) {
                  const javaClass = responseBody[i]
                  const javaClassName = javaClass["className"]
                  const methods = javaClass["methodStrings"]

                  _methodShownItem.push(
                    <div>
                      <h3> {javaClassName}</h3>
                      {
                        methods.map(method =>
                          <li>{method}</li>
                        )

                      }

                    </div>
                  );
                }
                setMethodShownItem(_methodShownItem)

              }}>


            </UploadApk>
            <div style={{ paddingTop: "20px" }}>
              <ProjectDescription></ProjectDescription>
            </div>

            <div>
              {
                methodShownItem.length > 0 && (<h2> Class And Methods (showing only {METHODS_SHOWN_LIMIT} methods)</h2>)
              }
              {methodShownItem}
            </div>
          </Row>

        </Container>
      </div>
    </>
  )
}

export default App
