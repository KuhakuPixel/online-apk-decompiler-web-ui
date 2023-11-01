import { useState } from 'react'
//import reactLogo from './assets/react.svg'
//import viteLogo from '/vite.svg'
//import './App.css'

function UploadApk() {

  return (
    <div>
      <iframe name="dummyframe" id="dummyframe" style={{ display: "none" }}></iframe>

      <form method="POST" enctype="multipart/form-data" action="http://localhost:8080/apk" target="dummyframe">
        <table>
          <tr><td>File to upload:</td><td><input type="file" name="file" /></td></tr>
          <tr><td><input type="submit" value="Upload" /></td></tr>
        </table>
      </form>
    </div>
  )
}
function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div >
        <h1> Online Apk Decompiler</h1>

        <UploadApk></UploadApk>
      </div>
    </>
  )
}

export default App
