import { useState } from 'react'
//import reactLogo from './assets/react.svg'
//import viteLogo from '/vite.svg'
//import './App.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';





function UploadApk() {

  return (
    <div >
      <iframe name="dummyframe" id="dummyframe" style={{ display: "none" }}></iframe>

      <Form method="POST" enctype="multipart/form-data" action="http://localhost:8080/apk" target="dummyframe">
        <Form.Group controlId="formFile" className="mb-3" >
          <Form.Label>Choose apk file and press upload</Form.Label>
          <Form.Control type="file" name="file" />
        </Form.Group>

        <Button variant="primary" type="submit">
          Upload
        </Button>

      </Form>
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

        <UploadApk></UploadApk>
      </div>
    </>
  )
}

export default App
