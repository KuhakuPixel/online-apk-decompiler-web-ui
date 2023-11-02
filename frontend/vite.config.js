import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true, // needed for the Docker Container port mapping to work
    port: 8000, // you can replace this port with any port
  }
})
