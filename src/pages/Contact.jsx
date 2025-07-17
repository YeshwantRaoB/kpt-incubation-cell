import React, { useState } from "react";
import { motion } from "framer-motion";
import { Mail, MapPin, Phone } from "lucide-react";

const Contact = () => {
  const [form, setForm] = useState({
    name: "",
    email: "",
    message: "",
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Connect to backend or form service (e.g., EmailJS/Formspree)
    alert("Message sent successfully!");
    setForm({ name: "", email: "", message: "" });
  };

  return (
    <div className="bg-gradient-to-br from-blue-950 to-blue-800 text-white py-20 px-4 md:px-10 overflow-hidden">
      <div className="max-w-7xl mx-auto grid lg:grid-cols-2 gap-12">
        {/* Left Column: Info & Form */}
        <motion.div
          initial={{ opacity: 0, x: -30 }}
          whileInView={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.6 }}
          className="space-y-10"
        >
          <div>
            <h2 className="text-4xl font-bold text-goldAccent mb-4">Get in Touch</h2>
            <p className="text-gray-300 text-sm">
              We’d love to hear from you! Whether you're a student, mentor, or enthusiast—reach out to us with your ideas, questions, or feedback.
            </p>
          </div>

          <div className="space-y-3 text-sm text-gray-300">
            <div className="flex items-center gap-3">
              <MapPin size={16} />
              Karnataka Govt. Polytechnic, Mangalore – 575001
            </div>
            <div className="flex items-center gap-3">
              <Phone size={16} />
              +91-98765-43210
            </div>
            <div className="flex items-center gap-3">
              <Mail size={16} />
              incubation@kgp.ac.in
            </div>
          </div>

          {/* Contact Form */}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm mb-1">Name</label>
              <input
                type="text"
                name="name"
                value={form.name}
                onChange={handleChange}
                required
                className="w-full px-4 py-2 rounded-lg bg-white/10 border border-blue-700 text-white focus:outline-none focus:ring-2 focus:ring-goldAccent"
              />
            </div>
            <div>
              <label className="block text-sm mb-1">Email</label>
              <input
                type="email"
                name="email"
                value={form.email}
                onChange={handleChange}
                required
                className="w-full px-4 py-2 rounded-lg bg-white/10 border border-blue-700 text-white focus:outline-none focus:ring-2 focus:ring-goldAccent"
              />
            </div>
            <div>
              <label className="block text-sm mb-1">Message</label>
              <textarea
                name="message"
                value={form.message}
                onChange={handleChange}
                rows={4}
                required
                className="w-full px-4 py-2 rounded-lg bg-white/10 border border-blue-700 text-white focus:outline-none focus:ring-2 focus:ring-goldAccent"
              />
            </div>
            <button
              type="submit"
              className="bg-goldAccent text-blue-950 px-6 py-2 rounded-full font-semibold hover:bg-yellow-400 transition shadow-md"
            >
              Send Message
            </button>
          </form>
        </motion.div>

        {/* Right Column: Google Map */}
        <motion.div
          initial={{ opacity: 0, x: 30 }}
          whileInView={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.6 }}
          className="rounded-2xl overflow-hidden border-4 border-blue-800 shadow-lg"
        >
          <iframe
            title="KPT Map"
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3889.2486630401704!2d74.85164257423679!3d12.891725716626107!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3ba35a145a8d7537%3A0x40972a862265bcb8!2sKarnataka%20(Govt.)%20Polytechnic!5e0!3m2!1sen!2sin!4v1752687929732!5m2!1sen!2sin"
            width="100%"
            height="100%"
            style={{ border: 0, minHeight: "450px" }}
            allowFullScreen=""
            loading="lazy"
            referrerPolicy="no-referrer-when-downgrade"
          ></iframe>
        </motion.div>
      </div>
    </div>
  );
};

export default Contact;
