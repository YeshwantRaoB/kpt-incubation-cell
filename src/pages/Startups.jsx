import React from "react";
import { motion } from "framer-motion";

const startups = [
  {
    name: "EcoTrack",
    description: "IoT-based waste tracking system to monitor and reduce environmental impact.",
    image: "/startups/ecotrack.jpg",
    domain: "Environment",
    year: "2024",
  },
  {
    name: "FarmNext",
    description: "AI-powered crop monitoring platform for small-scale farmers.",
    image: "/startups/farmnext.jpg",
    domain: "Agritech",
    year: "2023",
  },
  {
    name: "SafeStep",
    description: "Smart wearable for senior citizens with real-time fall detection.",
    image: "/startups/safestep.jpg",
    domain: "HealthTech",
    year: "2024",
  },
  {
    name: "EduSpark",
    description: "Interactive learning tools and gamified education for rural schools.",
    image: "/startups/eduspark.jpg",
    domain: "EdTech",
    year: "2023",
  },
];

const Startups = () => {
  return (
    <div className="bg-gradient-to-br from-blue-950 to-blue-800 text-white py-20 px-4 md:px-10 overflow-hidden">
      <div className="max-w-7xl mx-auto space-y-14">
        {/* Title */}
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="text-center max-w-3xl mx-auto"
        >
          <h2 className="text-4xl md:text-5xl font-extrabold text-goldAccent drop-shadow">
            Incubated Startups
          </h2>
          <p className="mt-4 text-gray-300 text-lg">
            A glimpse at the startups nurtured under the KPT Incubation Cell. From agriculture to education, our innovators are shaping the future.
          </p>
        </motion.div>

        {/* Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
          {startups.map((startup, index) => (
            <motion.div
              key={index}
              initial={{ opacity: 0, y: 40 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: index * 0.2 }}
              viewport={{ once: true }}
              className="bg-white/5 border border-blue-800 backdrop-blur-md rounded-xl shadow-lg hover:shadow-2xl transition-all overflow-hidden hover:scale-[1.02]"
            >
              {/* Image */}
              <img
                src={startup.image}
                alt={startup.name}
                className="w-full h-48 object-cover"
                loading="lazy"
              />

              {/* Content */}
              <div className="p-5 space-y-2">
                <h3 className="text-xl font-bold text-goldAccent">{startup.name}</h3>
                <p className="text-sm text-gray-300">{startup.description}</p>
                <div className="flex justify-between text-xs text-gray-400 pt-2">
                  <span>{startup.domain}</span>
                  <span>{startup.year}</span>
                </div>
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Startups;
