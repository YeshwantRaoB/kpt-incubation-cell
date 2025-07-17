import React, { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Link } from "react-router-dom";
import { ArrowRight } from "lucide-react";

const backgrounds = [
  "/slides/slide1.jpg",
  "/slides/slide2.jpg",
  "/slides/slide3.jpg",
  "/slides/slide4.jpg",
  "/slides/slide5.jpg",
];

const Home = () => {
  const [bgIndex, setBgIndex] = useState(0);
  const [typedText, setTypedText] = useState("");
  const fullText = "Empowering Tomorrow's Innovators";

useEffect(() => {
  let idx = 0;
  const interval = setInterval(() => {
    setTypedText(fullText.substring(0, idx + 1));
    idx++;
    if (idx === fullText.length) clearInterval(interval);
  }, 120);
  return () => clearInterval(interval);
}, []);



  useEffect(() => {
    const switcher = setInterval(() => {
      setBgIndex((prev) => (prev + 1) % backgrounds.length);
    }, 3000);
    return () => clearInterval(switcher);
  }, []);

  return (
    <div className="relative w-full h-screen overflow-hidden">
      <AnimatePresence>
        <motion.div
          key={bgIndex}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 1 }}
          className="absolute inset-0 bg-cover bg-center"
          style={{ backgroundImage: `url(${backgrounds[bgIndex]})` }}
        />
      </AnimatePresence>

      <div className="absolute inset-0 bg-black/50 backdrop-blur-sm" />

      <section className="relative z-10 flex flex-col items-center justify-center h-full text-center px-6">
        <motion.h1
          initial={{ opacity: 0, scale: 0.8 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 1 }}
          className="text-3xl md:text-5xl lg:text-6xl font-extrabold text-white drop-shadow-lg glow"
        >
          {typedText}
          <span className="blinking-cursor">|</span>
        </motion.h1>

        <motion.p
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1, delay: 1 }}
          className="mt-6 text-lg md:text-2xl text-gray-200 max-w-2xl"
        >
          Nurturing startups and transforming ideas into impactful ventures. Join us on the journey of innovation, mentorship, and success.
        </motion.p>

        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1, delay: 1.5 }}
          className="mt-10 flex flex-col sm:flex-row gap-4"
        >
          <Link
            to="/startups"
            className="px-8 py-4 rounded-full bg-goldAccent text-blue-950 font-semibold text-lg hover:shadow-lg hover:scale-105 transform transition"
          >
            Explore Startups <ArrowRight size={20} className="inline ml-2" />
          </Link>
          <Link
            to="/about"
            className="px-8 py-4 rounded-full border-2 border-goldAccent text-goldAccent font-semibold text-lg hover:bg-goldAccent hover:text-blue-950 transform transition"
          >
            Learn More
          </Link>
        </motion.div>
      </section>

      <style jsx>{`
        .glow {
          text-shadow: 0 0 8px rgba(255, 215, 0, 0.8), 0 0 16px rgba(255, 215, 0, 0.6);
        }
        .blinking-cursor {
          font-weight: 100;
          font-size: 1.2em;
          color: rgba(255, 255, 255, 0.75);
          animation: blink 1s infinite;
        }
        @keyframes blink {
          0%, 100% { opacity: 0; }
          50% { opacity: 1; }
        }
      `}</style>
    </div>
  );
};

export default Home;
