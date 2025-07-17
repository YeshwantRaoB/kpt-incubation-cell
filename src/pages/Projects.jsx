import React from "react";
import { motion } from "framer-motion";
import { Code2, Hammer, Cpu, Users } from "lucide-react";

const projects = [
  {
    year: "2022",
    title: "Smart Attendance System",
    icon: <Cpu size={24} />,
    description:
      "Developed a facial recognition-based attendance system integrated with a cloud dashboard for reporting.",
    image: "/projects/attendance.jpg",
  },
  {
    year: "2023",
    title: "Campus Energy Monitoring",
    icon: <Hammer size={24} />,
    description:
      "Designed a real-time power monitoring system for campus buildings using Arduino and IoT sensors.",
    image: "/projects/energy.jpg",
  },
  {
    year: "2024",
    title: "Student Connect Portal",
    icon: <Users size={24} />,
    description:
      "Built a web portal for managing mentorship, project submissions, and student collaboration within the incubation cell.",
    image: "/projects/portal.jpg",
  },
  {
    year: "2024",
    title: "AI Chatbot for Admissions",
    icon: <Code2 size={24} />,
    description:
      "Implemented an AI-powered chatbot to handle FAQs and admission guidance on the college website.",
    image: "/projects/chatbot.jpg",
  },
];

const Projects = () => {
  return (
    <div className="bg-gradient-to-br from-blue-950 to-blue-800 text-white py-20 px-4 md:px-10">
      <div className="max-w-7xl mx-auto space-y-14">
        {/* Title */}
        <motion.div
          initial={{ opacity: 0, y: 40 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="text-center max-w-3xl mx-auto"
        >
          <h2 className="text-4xl md:text-5xl font-extrabold text-goldAccent drop-shadow">
            Student Projects
          </h2>
          <p className="mt-4 text-gray-300 text-lg">
            Explore some of the innovative tech projects developed by students under the incubation program.
          </p>
        </motion.div>

        {/* Timeline-style Cards */}
        <div className="grid md:grid-cols-2 gap-10">
          {projects.map((project, index) => (
            <motion.div
              key={index}
              initial={{ opacity: 0, scale: 0.9 }}
              whileInView={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.6, delay: index * 0.1 }}
              viewport={{ once: true }}
              className="bg-white/5 border border-blue-800 rounded-2xl overflow-hidden shadow-md hover:shadow-xl transition-all hover:scale-[1.01]"
            >
              {/* Image */}
              <img
                src={project.image}
                alt={project.title}
                loading="lazy"
                className="w-full h-48 object-cover"
              />

              {/* Content */}
              <div className="p-5 space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-goldAccent font-medium">
                    {project.year}
                  </span>
                  <div className="text-goldAccent">{project.icon}</div>
                </div>
                <h3 className="text-xl font-bold text-goldAccent">{project.title}</h3>
                <p className="text-sm text-gray-300">{project.description}</p>
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Projects;
