import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path'; // path 모듈을 import

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      // 프록시 설정: /api로 시작하는 모든 요청을 Spring 서버로 포워딩
      '/api': {
        target: 'http://35.241.28.118:80', // Spring 서버 URL
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''), // 경로에서 /api를 제거
      },
    },
  },
  resolve: {
    alias: {
      // '@'를 'src' 폴더로 설정
      '@': path.resolve(__dirname, 'src'),
      // 필요한 경우 추가적인 별칭도 설정 가능
    },
  },
});