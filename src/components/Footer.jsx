import React from "react";
import { motion } from "framer-motion";
import {
  Facebook,
  Instagram,
  Linkedin,
  Mail,
  MapPin,
} from "lucide-react";
import { Link } from "react-router-dom";

const socialLinks = [
  {
    icon: <Instagram size={20} />,
    href: "https://www.instagram.com/kpt_mangalore/",
    label: "Instagram",
  },
  {
    icon: <Linkedin size={20} />,
    href: "https://www.linkedin.com/school/karnataka-govt-polytechnic/",
    label: "LinkedIn",
  },
  {
    icon: <Facebook size={20} />,
    href: "https://www.facebook.com/profile.php?id=110986748946908&_rdr",
    label: "Facebook",
  },
];

const Footer = () => {
  return (
    <motion.footer
      initial={{ opacity: 0, y: 40 }}
      whileInView={{ opacity: 1, y: 0 }}
      viewport={{ once: true }}
      transition={{ duration: 0.7, ease: "easeOut" }}
      className="bg-blue-950/70 backdrop-blur-md text-white pt-10 pb-6 px-4 md:px-10 mt-20 border-t border-blue-800"
    >
      <div className="max-w-7xl mx-auto grid md:grid-cols-3 gap-8">
        {/* Logo and Contact */}
        <div>
          <h2 className="text-goldAccent text-2xl font-bold">KPT Incubation Cell</h2>
          <p className="mt-3 text-sm text-gray-300">
            Empowering innovation and entrepreneurship at Karnataka Govt. Polytechnic, Mangalore.
          </p>
          <div className="mt-4 space-y-2 text-sm text-gray-400">
            <div className="flex items-center gap-2">
              <Mail size={16} />
              <a href="mailto:incubation@kpt.ac.in">incubation@kpt.ac.in</a>
            </div>
            <div className="flex items-center gap-2">
              <MapPin size={16} />
              <span>Mangalore, Karnataka – 575004</span>
            </div>
          </div>
        </div>

        {/* Quick Links */}
        <div>
          <h3 className="text-goldAccent text-xl font-semibold mb-4">Quick Links</h3>
          <ul className="space-y-2 text-sm text-gray-300">
            <li><Link to="/" className="hover:text-goldAccent transition">Home</Link></li>
            <li><Link to="/about" className="hover:text-goldAccent transition">About Us</Link></li>
            <li><Link to="/startups" className="hover:text-goldAccent transition">Our Startups</Link></li>
            <li><Link to="/projects" className="hover:text-goldAccent transition">Projects</Link></li>
            <li><Link to="/events" className="hover:text-goldAccent transition">Events</Link></li>
            <li><Link to="/contact" className="hover:text-goldAccent transition">Contact</Link></li>
          </ul>
        </div>

        {/* Socials */}
        <div>
          <h3 className="text-goldAccent text-xl font-semibold mb-4">Follow Us</h3>
          <div className="flex space-x-4">
            {socialLinks.map((link, idx) => (
              <motion.a
                key={idx}
                href={link.href}
                target="_blank"
                rel="noopener noreferrer"
                className="text-gray-300 hover:text-goldAccent transition-all"
                whileHover={{ scale: 1.2 }}
                whileTap={{ scale: 0.95 }}
                aria-label={link.label}
              >
                {link.icon}
              </motion.a>
            ))}
          </div>
        </div>
      </div>

      {/* Divider */}
      <div className="border-t border-blue-800 my-6" />

      {/* Bottom copyright */}
      <div className="text-center text-sm text-gray-400">
        © {new Date().getFullYear()} KPT Incubation Cell. All rights reserved.
      </div>
       {/* Bottom copyright */}
      <div className="text-center text-sm text-gray-400">
        Designed and Maintained by: Yeshwant Rao B.
      </div>
    </motion.footer>
  );
};

export default Footer;
