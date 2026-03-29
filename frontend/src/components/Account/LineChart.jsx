/* eslint-disable react/prop-types */
import React, { useState, useEffect } from "react";
import "chart.js/auto";
import { Line } from "react-chartjs-2";

export default function LineChart({ accountID, transHistory }) {
  const [labels, setLabels] = useState([]);
  const [amounts, setAmounts] = useState([]);

  useEffect(() => {
    const transactionLabels = [];
    const transactionAmounts = [];

    Object.entries(transHistory).forEach(([key, value]) => {
      transactionLabels.push(key);
      transactionAmounts.push(value.amount);
    });

    setLabels(transactionLabels);
    setAmounts(transactionAmounts);
  }, [transHistory]);

  const data = {
    labels: labels,
    datasets: [
      {
        label: accountID,
        data: amounts,
        borderColor: "rgb(112, 128, 144)",
        backgroundColor: "rgba(112, 128, 144, 0.5)",
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: "top",
      },
    },

    title: {
      display: true,
      text: "Transaction graph",
    },
  };

  return (
    <div>
      <Line data={data} options={options} />
    </div>
  );
}
