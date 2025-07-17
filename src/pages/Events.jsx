import React from "react";
import { motion } from "framer-motion";
import { CalendarDays, ArrowUpRight } from "lucide-react";

const events = [
  {
    date: "March 2024",
    title: "Startup Demo Day",
    description:
      "Students presented their startup prototypes to a panel of industry experts and mentors at KPT Auditorium.",
    link: "#",
  },
  {
    date: "Jan 2024",
    title: "AI & Innovation Bootcamp",
    description:
      "A 3-day bootcamp conducted in collaboration with NASSCOM, focusing on AI tools and MVP building.",
    link: "#",
  },
  {
    date: "Oct 2023",
    title: "HackKPT 2023",
    description:
      "48-hour hackathon focused on solving real-world problems in education and environment.",
    link: "#",
  },
  {
    date: "July 2023",
    title: "Mentor Connect Meetup",
    description:
      "An informal gathering for students to interact with startup mentors and alumni founders.",
    link: "#",
  },
];

const Events = () => {
  return (
    <div className="bg-gradient-to-br from-blue-950 to-blue-800 text-white py-20 px-4 md:px-10 overflow-hidden">
      <div className="max-w-6xl mx-auto">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: 40 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="text-center mb-14"
        >
          <h2 className="text-4xl md:text-5xl font-extrabold text-goldAccent drop-shadow">
            Events & Workshops
          </h2>
          <p className="mt-4 text-gray-300 text-lg max-w-2xl mx-auto">
            Discover our recent and upcoming events that empower innovation, collaboration, and growth.
          </p>
        </motion.div>

        {/* Timeline */}
        <div className="relative space-y-12">
          {events.map((event, index) => (
            <motion.div
              key={index}
              initial={{ opacity: 0, x: -30 }}
              whileInView={{ opacity: 1, x: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5, delay: index * 0.1 }}
            >
              {/* Card */}
              <div className="bg-white/5 border border-blue-800 backdrop-blur-xl rounded-xl p-6 shadow-md hover:shadow-xl transition-all hover:scale-[1.01]">
                <div className="flex items-center gap-2 text-sm text-goldAccent mb-1">
                  <CalendarDays size={16} />
                  <span className="ml-2">{event.date}</span>
                </div>
                <h3 className="text-xl font-bold text-white mb-2">{event.title}</h3>
                <p className="text-sm text-gray-300">{event.description}</p>
                {event.link && (
                  <a
                    href={event.link}
                    className="inline-flex items-center text-goldAccent hover:underline text-sm mt-3"
                  >
                    View More <ArrowUpRight className="ml-1" size={16} />
                  </a>
                )}
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Events;
