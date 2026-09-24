/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class', 
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          primary: 'var(--color-brand-primary)',
          soft: 'var(--color-brand-primary-soft)',
          secondary: 'var(--color-brand-secondary)',
        },
        surface: {
          app: 'var(--bg-app)',
          DEFAULT: 'var(--bg-surface)',
          elevated: 'var(--bg-surface-elevated)', 
        },
        content: {
          primary: 'var(--text-primary)',
          secondary: 'var(--text-secondary)',
          muted: 'var(--text-muted)',
        },
        border: {
          subtle: 'var(--border-subtle)',
        },
        status: {
          error: 'var(--status-error)',
          success: 'var(--status-success)',
        }
      },
      borderRadius: {
        'brand': 'var(--radius-md)',
      },
      boxShadow: {
        'card': 'var(--shadow-card)',
      }
    },
  },
  plugins: [],
}