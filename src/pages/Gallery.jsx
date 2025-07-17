import React from "react";
import { motion } from "framer-motion";
import { PhotoProvider, PhotoView } from "react-photo-view";

const images = [
  "/gallery/1.jpg",
  "/gallery/2.jpg",
  "/gallery/3.jpg",
  "/gallery/4.jpg",
  "/gallery/5.jpg",
  "/gallery/6.jpg",
  "/gallery/7.jpg",
  "/gallery/8.jpg",
];

const Gallery = () => {
  return (
    <div className="bg-gradient-to-br from-blue-950 to-blue-800 text-white py-20 px-4 md:px-10">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="text-center mb-14"
        >
          <h2 className="text-4xl md:text-5xl font-extrabold text-goldAccent drop-shadow">
            Photo Gallery
          </h2>
          <p className="mt-4 text-gray-300 text-lg max-w-2xl mx-auto">
            Glimpses of events, workshops, demos, and startup showcases held under the KPT Incubation Cell.
          </p>
        </motion.div>

        {/* Gallery Grid */}
        <PhotoProvider>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
            {images.map((img, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, scale: 0.95 }}
                whileInView={{ opacity: 1, scale: 1 }}
                viewport={{ once: true }}
                transition={{ duration: 0.4, delay: index * 0.1 }}
                className="rounded-xl overflow-hidden shadow-lg hover:shadow-2xl border border-blue-800 backdrop-blur-md"
              >
                <PhotoView src={img}>
                  <img
                    src={img}
                    alt={`gallery-${index + 1}`}
                    loading="lazy"
                    className="w-full h-64 object-cover transform hover:scale-105 transition duration-300 cursor-pointer"
                  />
                </PhotoView>
              </motion.div>
            ))}
          </div>
        </PhotoProvider>
      </div>
    </div>
  );
};

export default Gallery;
