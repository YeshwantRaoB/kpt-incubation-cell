/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: "class",
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primaryBlue: '#0B1E3F',
        goldAccent: '#FFD700',
        blueDark: '#0B1E3F',
        lightBlue: '#3B82F6'
      }
    },
  },
  plugins: [],
}
