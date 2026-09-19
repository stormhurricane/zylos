/** @type {import('tailwindcss').Config} */
export default {
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
        'brand': 'var(--radius-md)', // Ergibt rounded-brand (12px)
      },
      boxShadow: {
        'card': 'var(--shadow-card)', // Ergibt shadow-card
      }
    },
  },
  plugins: [],
}