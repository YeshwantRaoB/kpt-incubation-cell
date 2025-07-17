import React, { useState } from "react";
import { motion } from "framer-motion";
import { Lock, Mail, Eye, EyeOff } from "lucide-react";

const AdminLogin = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");

  const togglePassword = () => setShowPassword((prev) => !prev);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Placeholder authentication logic
    if (form.email === "admin@kgp.ac.in" && form.password === "admin123") {
      alert("Login successful!");
      // Redirect or set session here
    } else {
      setError("Invalid email or password.");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-950 to-blue-800 flex items-center justify-center px-4">
      <motion.div
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="w-full max-w-md bg-white/5 backdrop-blur-xl border border-blue-800 rounded-2xl shadow-lg p-8"
      >
        <div className="text-center mb-8">
          <h2 className="text-3xl font-bold text-goldAccent">Admin Login</h2>
          <p className="text-sm text-gray-300 mt-1">
            Authorized access only.
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="text-sm text-white block mb-1">Email</label>
            <div className="flex items-center px-3 py-2 rounded-lg bg-white/10 border border-blue-700 focus-within:ring-2 focus-within:ring-goldAccent">
              <Mail size={18} className="text-goldAccent mr-2" />
              <input
                type="email"
                name="email"
                required
                value={form.email}
                onChange={handleChange}
                placeholder="admin@kgp.ac.in"
                className="bg-transparent outline-none w-full text-white text-sm"
              />
            </div>
          </div>

          <div>
            <label className="text-sm text-white block mb-1">Password</label>
            <div className="flex items-center px-3 py-2 rounded-lg bg-white/10 border border-blue-700 focus-within:ring-2 focus-within:ring-goldAccent">
              <Lock size={18} className="text-goldAccent mr-2" />
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                required
                value={form.password}
                onChange={handleChange}
                placeholder="Enter password"
                className="bg-transparent outline-none w-full text-white text-sm"
              />
              <button
                type="button"
                onClick={togglePassword}
                className="text-goldAccent focus:outline-none ml-2"
              >
                {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          {error && (
            <div className="text-sm text-red-400 font-medium">{error}</div>
          )}

          <button
            type="submit"
            className="w-full bg-goldAccent text-blue-950 font-semibold py-2 rounded-full hover:bg-yellow-400 transition shadow-md"
          >
            Login
          </button>
        </form>
      </motion.div>
    </div>
  );
};

export default AdminLogin;
