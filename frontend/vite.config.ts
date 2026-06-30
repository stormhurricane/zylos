/// <reference types="vitest" />
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    strictPort: true,
    host: true,
    watch: {
      ignored: ['**/node_modules/**', '**/dist/**'],
    },
  },
  test: {
    environment: 'jsdom',
    globals: true, 
    setupFiles: './src/test/setup.ts',
    
    server: {
      deps: {
        inline: ['msw'],
      },
    },
    
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html', 'lcov'],
      exclude: [
        'src/test/**',
        'src/mocks/**',
        'node_modules/**',
        '**/*.d.ts'
      ]
    },
  },
})