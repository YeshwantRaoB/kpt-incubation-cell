import React from "react";
import { motion } from "framer-motion";
import { Lightbulb, Users, Target } from "lucide-react";

const infoCards = [
  {
    title: "Our Mission",
    icon: <Target size={28} />,
    content:
      "To empower student innovators and entrepreneurs with the support, mentorship, and resources needed to build scalable, impactful ventures.",
  },
  {
    title: "Our Vision",
    icon: <Lightbulb size={28} />,
    content:
      "To establish Karnataka Govt. Polytechnic Mangalore as a leading hub for innovation and startup incubation in the state.",
  },
  {
    title: "Our Objective",
    icon: <Users size={28} />,
    content:
      "To foster an entrepreneurial ecosystem among students by promoting idea validation, prototyping, business model development, and industry exposure.",
  },
];

const timeline = [
  {
    year: "2022",
    event: "Incubation Cell was established at KPT Mangalore to support student startups.",
  },
  {
    year: "2023",
    event: "First batch of 5 student startups incubated and received mentorship.",
  },
  {
    year: "2024",
    event: "Expanded collaborations with industry experts and tech mentors.",
  },
];

const About = () => {
  return (
    <div className="bg-gradient-to-br from-blue-950 via-blue-900 to-blue-800 text-white py-20 px-4 md:px-10 overflow-hidden">
      <div className="max-w-7xl mx-auto space-y-20">
        {/* Header Section */}
        <motion.div
          initial={{ opacity: 0, y: 50 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8 }}
          className="text-center max-w-3xl mx-auto"
        >
          <h2 className="text-4xl md:text-5xl font-extrabold text-goldAccent drop-shadow-md">
            About Our Incubation Cell
          </h2>
          <p className="mt-4 text-lg text-gray-300">
            Karnataka Govt. Polytechnic's Incubation Cell is a platform for student innovators and tech enthusiasts to transform their ideas into viable startups.
          </p>
        </motion.div>

        {/* Info Cards Section */}
        <div className="grid md:grid-cols-3 gap-8">
          {infoCards.map((card, index) => (
            <motion.div
              key={index}
              initial={{ opacity: 0, y: 60 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.6, delay: index * 0.2 }}
              className="bg-white/5 backdrop-blur-lg rounded-2xl p-6 border border-blue-800 shadow-md hover:shadow-xl hover:scale-[1.02] transition-all"
            >
              <div className="text-goldAccent mb-4">{card.icon}</div>
              <h3 className="text-xl font-semibold text-goldAccent mb-2">{card.title}</h3>
              <p className="text-gray-300 text-sm">{card.content}</p>
            </motion.div>
          ))}
        </div>

        {/* Timeline Section */}
        <div className="relative">
          <motion.h3
            initial={{ opacity: 0 }}
            whileInView={{ opacity: 1 }}
            viewport={{ once: true }}
            transition={{ delay: 0.2, duration: 0.5 }}
            className="text-3xl font-bold text-center text-goldAccent mb-10"
          >
            Our Journey
          </motion.h3>

          <div className="border-l-2 border-goldAccent pl-6 relative space-y-10">
            {timeline.map((item, idx) => (
              <motion.div
                key={idx}
                initial={{ opacity: 0, x: -30 }}
                whileInView={{ opacity: 1, x: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.5, delay: idx * 0.2 }}
                className="relative"
              >
                <div className="absolute -left-[13px] top-1.5 w-4 h-4 rounded-full bg-goldAccent border-2 border-white"></div>
                <p className="text-lg font-semibold text-white">{item.year}</p>
                <p className="text-gray-300 text-sm">{item.event}</p>
              </motion.div>
            ))}
          </div>
        </div>

        {/* CTA Section */}
        <motion.div
          initial={{ scale: 0.95, opacity: 0 }}
          whileInView={{ scale: 1, opacity: 1 }}
          transition={{ duration: 0.6, delay: 0.3 }}
          className="text-center mt-16"
        >
          <h4 className="text-xl font-semibold text-goldAccent mb-4">Want to be a part of our journey?</h4>
          <a
            href="/contact"
            className="inline-block px-6 py-3 rounded-full bg-goldAccent text-blue-950 font-semibold hover:bg-yellow-400 transition shadow-lg"
          >
            Contact Us
          </a>
        </motion.div>
      </div>
    </div>
  );
};

export default About;
