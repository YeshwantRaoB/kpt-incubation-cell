// src/components/AnimatedCard.jsx
import { motion } from "framer-motion";
import { useInView } from "react-intersection-observer";

const AnimatedCard = ({ children, delay = 0 }) => {
  const [ref, inView] = useInView({
    triggerOnce: true,
    threshold: 0.1,
  });

  return (
    <motion.div
      ref={ref}
      initial={{ opacity: 0, y: 50 }}
      animate={inView ? { opacity: 1, y: 0 } : {}}
      transition={{ duration: 0.7, delay }}
      className="bg-white/10 backdrop-blur-lg p-6 rounded-2xl shadow-lg"
    >
      {children}
    </motion.div>
  );
};

export default AnimatedCard;
